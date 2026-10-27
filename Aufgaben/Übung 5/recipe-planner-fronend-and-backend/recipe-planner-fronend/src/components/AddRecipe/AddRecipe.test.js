import React from 'react';
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import {MemoryRouter, Route, Routes} from 'react-router-dom';
import axios from 'axios';
import AddRecipe from './AddRecipe';

jest.mock('axios');

beforeEach(() => jest.resetAllMocks());

function renderForm(initialPath = '/new-menues') {
    render(
        <MemoryRouter initialEntries={[initialPath]}>
            <Routes>
                <Route path="/new-menues" element={<AddRecipe />} />
                <Route path="/recipes/:recipeId/edit" element={<AddRecipe />} />
                <Route path="/" element={<h1>Recipe list</h1>} />
            </Routes>
        </MemoryRouter>
    );
}

function submitForm() {
    fireEvent.submit(screen.getByRole('button', {name: 'Rezept speichern'}).closest('form'));
}

test('sends recipe and remaining ingredients in backend format, then opens the list', async () => {
    axios.post.mockResolvedValue({data: {id: 'saved-id'}});
    renderForm();
    fireEvent.change(screen.getByLabelText('Rezeptname (Pflichtfeld)'), {target: {value: ' Pasta '}});
    fireEvent.change(screen.getByLabelText('Beschreibung (optional)'), {target: {value: 'With tomatoes'}});
    fireEvent.change(screen.getByLabelText('Bildadresse (optional)'), {target: {value: 'https://example.com/pasta.jpg'}});
    fireEvent.click(screen.getByRole('button', {name: 'Zutat hinzufügen'}));
    fireEvent.change(screen.getByLabelText('Zutat'), {target: {value: ' Tomato '}});
    fireEvent.change(screen.getByLabelText('Einheit'), {target: {value: 'GRAMM'}});
    fireEvent.change(screen.getByLabelText('Menge'), {target: {value: '200'}});
    fireEvent.click(screen.getByRole('button', {name: 'Zutat hinzufügen'}));
    fireEvent.click(screen.getAllByRole('button', {name: 'Zutat entfernen'})[1]);
    submitForm();

    expect(axios.post).toHaveBeenCalledTimes(1);
    expect(axios.post).toHaveBeenCalledWith('http://localhost:8080/api/recipes', {
        name: 'Pasta', description: 'With tomatoes', imageUrl: 'https://example.com/pasta.jpg',
        ingredients: [{name: 'Tomato', comment: '', unit: 'GRAMM', amount: 200}]
    });
    expect(await screen.findByRole('heading', {name: 'Recipe list'})).toBeInTheDocument();
});

test('rejects a blank name and invalid ingredient quantities without sending a request', () => {
    renderForm();
    fireEvent.change(screen.getByLabelText('Rezeptname (Pflichtfeld)'), {target: {value: '   '}});
    submitForm();
    expect(screen.getByRole('alert')).toBeInTheDocument();
    expect(axios.post).not.toHaveBeenCalled();

    fireEvent.change(screen.getByLabelText('Rezeptname (Pflichtfeld)'), {target: {value: 'Pasta'}});
    fireEvent.click(screen.getByRole('button', {name: 'Zutat hinzufügen'}));
    fireEvent.change(screen.getByLabelText('Zutat'), {target: {value: 'Tomato'}});
    for (const amount of ['0', '-1', '1.5', '2147483648']) {
        fireEvent.change(screen.getByLabelText('Menge'), {target: {value: amount}});
        submitForm();
        expect(axios.post).not.toHaveBeenCalled();
    }
});

test('keeps input after a failed request and allows retry while blocking duplicate submissions', async () => {
    let rejectRequest;
    axios.post.mockImplementationOnce(() => new Promise((resolve, reject) => { rejectRequest = reject; }));
    renderForm();
    fireEvent.change(screen.getByLabelText('Rezeptname (Pflichtfeld)'), {target: {value: 'Pasta'}});
    submitForm();
    expect(screen.getByRole('button', {name: 'Wird gespeichert...'})).toBeDisabled();
    fireEvent.submit(screen.getByRole('button', {name: 'Wird gespeichert...'}).closest('form'));
    expect(axios.post).toHaveBeenCalledTimes(1);
    rejectRequest(new Error('Network error'));

    expect(await screen.findByRole('alert')).toHaveTextContent('konnte nicht gespeichert werden');
    expect(screen.getByLabelText('Rezeptname (Pflichtfeld)')).toHaveValue('Pasta');
    await waitFor(() => expect(screen.getByRole('button', {name: 'Rezept speichern'})).not.toBeDisabled());
    axios.post.mockResolvedValueOnce({data: {id: 'saved-id'}});
    submitForm();
    expect(await screen.findByRole('heading', {name: 'Recipe list'})).toBeInTheDocument();
    expect(axios.post).toHaveBeenCalledTimes(2);
});
const savedRecipe = {
    id: 'recipe-id', name: 'Pasta', description: 'Mit Tomaten', imageUrl: '',
    ingredients: [{id: 'ingredient-id', name: 'Tomate', comment: 'Frisch', unit: 'GRAMM', amount: 200}]
};

test('loads an existing recipe and saves changes with PUT using the same recipe id', async () => {
    axios.get.mockResolvedValueOnce({data: savedRecipe});
    axios.put.mockResolvedValueOnce({data: savedRecipe});
    renderForm('/recipes/recipe-id/edit');
    expect(screen.getByRole('status')).toHaveTextContent('Rezept wird geladen');
    expect(await screen.findByRole('heading', {name: 'Rezept bearbeiten'})).toBeInTheDocument();
    expect(axios.get).toHaveBeenCalledWith('http://localhost:8080/api/recipes/recipe/recipe-id');
    expect(screen.getByLabelText('Rezeptname (Pflichtfeld)')).toHaveValue('Pasta');
    expect(screen.getByLabelText('Zutat')).toHaveValue('Tomate');
    fireEvent.change(screen.getByLabelText('Rezeptname (Pflichtfeld)'), {target: {value: 'Tomatenpasta'}});
    fireEvent.change(screen.getByLabelText('Menge'), {target: {value: '300'}});
    fireEvent.click(screen.getByRole('button', {name: 'Änderungen speichern'}));
    expect(await screen.findByRole('heading', {name: 'Recipe list'})).toBeInTheDocument();
    expect(axios.put).toHaveBeenCalledWith('http://localhost:8080/api/recipes/recipe/recipe-id', {
        name: 'Tomatenpasta', description: 'Mit Tomaten', imageUrl: '',
        ingredients: [{name: 'Tomate', comment: 'Frisch', unit: 'GRAMM', amount: 300}]
    });
    expect(axios.post).not.toHaveBeenCalled();
});

test('shows a loading error instead of an empty edit form and allows returning to the list', async () => {
    axios.get.mockRejectedValueOnce(new Error('Not found'));
    renderForm('/recipes/missing/edit');
    expect(await screen.findByRole('alert')).toHaveTextContent('nicht geladen');
    expect(screen.queryByRole('button', {name: 'Änderungen speichern'})).not.toBeInTheDocument();
    fireEvent.click(screen.getByRole('link', {name: 'Zurück zur Liste'}));
    expect(await screen.findByRole('heading', {name: 'Recipe list'})).toBeInTheDocument();
    expect(axios.put).not.toHaveBeenCalled();
});

test('keeps edits when saving fails and cancels without another update', async () => {
    axios.get.mockResolvedValueOnce({data: savedRecipe});
    axios.put.mockRejectedValueOnce(new Error('Network error'));
    renderForm('/recipes/recipe-id/edit');
    await screen.findByRole('heading', {name: 'Rezept bearbeiten'});
    fireEvent.change(screen.getByLabelText('Rezeptname (Pflichtfeld)'), {target: {value: 'Geändert'}});
    fireEvent.click(screen.getByRole('button', {name: 'Änderungen speichern'}));
    expect(await screen.findByRole('alert')).toHaveTextContent('konnte nicht gespeichert werden');
    expect(screen.getByLabelText('Rezeptname (Pflichtfeld)')).toHaveValue('Geändert');
    fireEvent.click(screen.getByRole('button', {name: 'Abbrechen'}));
    expect(await screen.findByRole('heading', {name: 'Recipe list'})).toBeInTheDocument();
    expect(axios.put).toHaveBeenCalledTimes(1);
});
