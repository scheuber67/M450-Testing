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
    fireEvent.submit(screen.getByRole('button', {name: 'Submit'}).closest('form'));
}

test('sends recipe and remaining ingredients in backend format, then opens the list', async () => {
    axios.post.mockResolvedValue({data: {id: 'saved-id'}});
    renderForm();
    fireEvent.change(screen.getByLabelText('Recipe Name:'), {target: {value: ' Pasta '}});
    fireEvent.change(screen.getByLabelText('Description:'), {target: {value: 'With tomatoes'}});
    fireEvent.change(screen.getByLabelText('Image URL:'), {target: {value: 'https://example.com/pasta.jpg'}});
    fireEvent.click(screen.getByRole('button', {name: 'Add Ingredient'}));
    fireEvent.change(screen.getByLabelText('Ingredient'), {target: {value: ' Tomato '}});
    fireEvent.change(screen.getByLabelText('Unit'), {target: {value: 'GRAMM'}});
    fireEvent.change(screen.getByLabelText('Quantity'), {target: {value: '200'}});
    fireEvent.click(screen.getByRole('button', {name: 'Add Ingredient'}));
    fireEvent.click(screen.getAllByRole('button', {name: 'Remove ingredient'})[1]);
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
    fireEvent.change(screen.getByLabelText('Recipe Name:'), {target: {value: '   '}});
    submitForm();
    expect(screen.getByRole('alert')).toBeInTheDocument();
    expect(axios.post).not.toHaveBeenCalled();

    fireEvent.change(screen.getByLabelText('Recipe Name:'), {target: {value: 'Pasta'}});
    fireEvent.click(screen.getByRole('button', {name: 'Add Ingredient'}));
    fireEvent.change(screen.getByLabelText('Ingredient'), {target: {value: 'Tomato'}});
    for (const amount of ['0', '-1', '1.5', '2147483648']) {
        fireEvent.change(screen.getByLabelText('Quantity'), {target: {value: amount}});
        submitForm();
        expect(axios.post).not.toHaveBeenCalled();
    }
});

test('keeps input after a failed request and allows retry while blocking duplicate submissions', async () => {
    let rejectRequest;
    axios.post.mockImplementationOnce(() => new Promise((resolve, reject) => { rejectRequest = reject; }));
    renderForm();
    fireEvent.change(screen.getByLabelText('Recipe Name:'), {target: {value: 'Pasta'}});
    submitForm();
    expect(screen.getByRole('button', {name: 'Saving...'})).toBeDisabled();
    fireEvent.submit(screen.getByRole('button', {name: 'Saving...'}).closest('form'));
    expect(axios.post).toHaveBeenCalledTimes(1);
    rejectRequest(new Error('Network error'));

    expect(await screen.findByRole('alert')).toHaveTextContent('could not be saved');
    expect(screen.getByLabelText('Recipe Name:')).toHaveValue('Pasta');
    await waitFor(() => expect(screen.getByRole('button', {name: 'Submit'})).not.toBeDisabled());
    axios.post.mockResolvedValueOnce({data: {id: 'saved-id'}});
    submitForm();
    expect(await screen.findByRole('heading', {name: 'Recipe list'})).toBeInTheDocument();
    expect(axios.post).toHaveBeenCalledTimes(2);
});
