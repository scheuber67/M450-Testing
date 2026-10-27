import React, {useState} from 'react';
import './AddRecipe.css';
import {Alert, Form, Button} from 'react-bootstrap';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import AddIngredient from '../AddIngredient/AddIngredient';

function AddRecipe() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '', description: '', imageUrl: '', ingredients: []
    });
    const [nextId, setNextId] = useState(1);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    const updateField = (event) => {
        setFormData({...formData, [event.target.name]: event.target.value});
    };

    const addIngredient = () => {
        setFormData({...formData, ingredients: [...formData.ingredients, {
            listId: nextId, name: '', unit: 'PIECE', amount: ''
        }]});
        setNextId(nextId + 1);
    };

    const updateIngredient = (updated) => {
        setFormData({...formData, ingredients: formData.ingredients.map(ingredient =>
            ingredient.listId === updated.listId ? updated : ingredient
        )});
    };

    const removeIngredient = (removed) => {
        setFormData({...formData, ingredients: formData.ingredients.filter(ingredient =>
            ingredient.listId !== removed.listId
        )});
    };

    const saveRecipe = async (event) => {
        event.preventDefault();
        if (saving) return;

        if (!formData.name.trim() || formData.ingredients.some(ingredient =>
            !ingredient.name.trim() || !Number.isInteger(Number(ingredient.amount)) ||
            Number(ingredient.amount) < 1 || Number(ingredient.amount) > 2147483647
        )) {
            setError('Enter a recipe name and complete each ingredient with a positive whole-number quantity.');
            return;
        }

        const recipe = {
            name: formData.name.trim(),
            description: formData.description.trim(),
            imageUrl: formData.imageUrl.trim(),
            ingredients: formData.ingredients.map(ingredient => ({
                name: ingredient.name.trim(),
                comment: '',
                unit: ingredient.unit,
                amount: Number(ingredient.amount)
            }))
        };

        setSaving(true);
        setError('');
        try {
            await axios.post('http://localhost:8080/api/recipes', recipe);
            navigate('/');
        } catch {
            setError('The recipe could not be saved. Please try again.');
        } finally {
            setSaving(false);
        }
    };

    return (
        <div className="bg">
            <div className="m-3">
                <h1 className="h3 bg-dark text-bg-primary mt-2">Add Recipe</h1>
                <Form onSubmit={saveRecipe}>
                    {error && <Alert variant="danger" role="alert">{error}</Alert>}
                    <fieldset disabled={saving}>
                        <Form.Group className="mb-1" controlId="recipeName">
                            <Form.Label>Recipe Name:</Form.Label>
                            <Form.Control name="name" value={formData.name} onChange={updateField} required />
                        </Form.Group>
                        <Form.Group className="mb-1" controlId="recipeDescription">
                            <Form.Label>Description:</Form.Label>
                            <Form.Control name="description" value={formData.description} onChange={updateField} />
                        </Form.Group>
                        <Form.Group className="mb-1 mb-5" controlId="recipeImageUrl">
                            <Form.Label>Image URL:</Form.Label>
                            <Form.Control name="imageUrl" type="url" value={formData.imageUrl} onChange={updateField} />
                        </Form.Group>
                        {formData.ingredients.map(ingredient => (
                            <AddIngredient key={ingredient.listId} ingredient={ingredient}
                                updateIngredient={updateIngredient} removeIngredient={removeIngredient} />
                        ))}
                        <Button type="button" variant="warning" onClick={addIngredient}>Add Ingredient</Button>
                        <Button variant="primary" type="submit" className="mb-5">
                            {saving ? 'Saving...' : 'Submit'}
                        </Button>
                    </fieldset>
                </Form>
            </div>
        </div>
    );
}

export default AddRecipe;
