import React from 'react';
import {fireEvent, render, screen, waitFor} from '@testing-library/react';
import {MemoryRouter, Route, Routes} from 'react-router-dom';
import axios from 'axios';
import AddRecipe from './AddRecipe';

jest.mock('axios');

beforeEach(() => jest.resetAllMocks());

function renderForm() {
    render(
        <MemoryRouter initialEntries={['/new-menues']}>
            <Routes>
                <Route path="/new-menues" element={<AddRecipe />} />
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
