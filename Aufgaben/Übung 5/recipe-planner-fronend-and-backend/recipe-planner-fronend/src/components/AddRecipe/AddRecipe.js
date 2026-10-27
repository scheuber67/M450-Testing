import React, {useEffect, useState} from 'react';
import './AddRecipe.css';
import {Alert, Form, Button} from 'react-bootstrap';
import {Link, useNavigate, useParams} from 'react-router-dom';
import axios from 'axios';
import AddIngredient from '../AddIngredient/AddIngredient';

function AddRecipe() {
    const navigate = useNavigate();
    const {recipeId} = useParams();
    const editing = Boolean(recipeId);
    const [loading, setLoading] = useState(editing);
    const [loadError, setLoadError] = useState('');
    const [formData, setFormData] = useState({
        name: '', description: '', imageUrl: '', ingredients: []
    });
    const [nextId, setNextId] = useState(1);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!recipeId) {
            setFormData({name: '', description: '', imageUrl: '', ingredients: []});
            setLoading(false);
            setLoadError('');
            return;
        }
        let active = true;
        setLoading(true);
        setLoadError('');
        axios.get(`http://localhost:8080/api/recipes/recipe/${recipeId}`)
            .then(({data}) => {
                if (!active) return;
                if (!data || !data.id) throw new Error('Recipe not found');
                const ingredients = (data.ingredients || []).map((ingredient, index) => ({
                    ...ingredient, listId: index + 1
                }));
                setFormData({name: data.name || '', description: data.description || '',
                    imageUrl: data.imageUrl || '', ingredients});
                setNextId(ingredients.length + 1);
            })
            .catch(() => {
                if (active) setLoadError('Das Rezept konnte nicht geladen werden. Bitte zurück zur Liste gehen.');
            })
            .finally(() => {
                if (active) setLoading(false);
            });
        return () => { active = false; };
    }, [recipeId]);
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
            setError('Bitte einen Rezeptnamen eingeben und jede Zutat mit einer positiven ganzen Menge ausfüllen.');
            return;
        }

        const recipe = {
            name: formData.name.trim(),
            description: formData.description.trim(),
            imageUrl: formData.imageUrl.trim(),
            ingredients: formData.ingredients.map(ingredient => ({
                name: ingredient.name.trim(),
                comment: ingredient.comment || '',
                unit: ingredient.unit,
                amount: Number(ingredient.amount)
            }))
        };

        setSaving(true);
        setError('');
        try {
            if (editing) {
                await axios.put(`http://localhost:8080/api/recipes/recipe/${recipeId}`, recipe);
            } else {
                await axios.post('http://localhost:8080/api/recipes', recipe);
            }
            navigate('/');
        } catch {
            setError('Das Rezept konnte nicht gespeichert werden. Bitte erneut versuchen.');
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <div className="add-recipe" role="status">Rezept wird geladen...</div>;
    if (loadError) return (
        <div className="add-recipe">
            <Alert variant="danger" role="alert">{loadError}</Alert>
            <Link to="/">Zurück zur Liste</Link>
        </div>
    );

    return (
        <div className="add-recipe">
            <div className="add-recipe-panel">
                <h1>{editing ? 'Rezept bearbeiten' : 'Rezept hinzufügen'}</h1>
                <Form onSubmit={saveRecipe}>
                    {error && <Alert variant="danger" role="alert">{error}</Alert>}
                    <fieldset disabled={saving}>
                        <Form.Group className="mb-1" controlId="recipeName">
                            <Form.Label>Rezeptname (Pflichtfeld)</Form.Label>
                            <Form.Control name="name" placeholder="z. B. Spaghetti mit Tomatensauce" value={formData.name} onChange={updateField} required />
                        </Form.Group>
                        <Form.Group className="mb-1" controlId="recipeDescription">
                            <Form.Label>Beschreibung (optional)</Form.Label>
                            <Form.Control as="textarea" rows={3} name="description" placeholder="Beschreibe dein Rezept oder die Zubereitung." value={formData.description} onChange={updateField} />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="recipeImageUrl">
                            <Form.Label>Bildadresse (optional)</Form.Label>
                            <Form.Control name="imageUrl" type="url" placeholder="https://beispiel.ch/rezept.jpg" value={formData.imageUrl} onChange={updateField} />
                        </Form.Group>
                        <h2>Zutaten</h2>
                        <div className="form-text mb-3">Füge Zutaten mit Name, Einheit und Menge hinzu. Die Menge muss eine ganze Zahl sein.</div>
                        {formData.ingredients.map(ingredient => (
                            <AddIngredient key={ingredient.listId} ingredient={ingredient}
                                updateIngredient={updateIngredient} removeIngredient={removeIngredient} />
                        ))}
                        <div className="add-recipe-actions">
                            <Button type="button" variant="outline-secondary" onClick={addIngredient}>Zutat hinzufügen</Button>
                            <Button variant="primary" type="submit">
                                {saving ? 'Wird gespeichert...' : editing ? 'Änderungen speichern' : 'Rezept speichern'}
                            </Button>
                            <Button as={Link} to="/" variant="outline-secondary" disabled={saving}>Abbrechen</Button>
                        </div>
                    </fieldset>
                </Form>
            </div>
        </div>
    );
}

export default AddRecipe;
