import React from 'react';
import './AddIngredient.css';
import {Button, Col, Form, Row} from 'react-bootstrap';

const AddIngredient = ({ingredient, updateIngredient, removeIngredient}) => {
    const updateField = (event) => {
        updateIngredient({...ingredient, [event.target.name]: event.target.value});
    };

    return (
        <Row className="ingredient-row align-items-end">
            <Col xs={12} sm>
                <Form.Group className="mb-1" controlId={`ingredientName-${ingredient.listId}`}>
                    <Form.Label>Zutat</Form.Label>
                    <Form.Control name="name" placeholder="z. B. Mehl" value={ingredient.name} onChange={updateField} required />
                </Form.Group>
            </Col>
            <Col xs={12} sm>
                <Form.Group className="mb-1" controlId={`ingredientUnit-${ingredient.listId}`}>
                    <Form.Label>Einheit</Form.Label>
                    <Form.Select name="unit" value={ingredient.unit} onChange={updateField}>
                        {Object.entries({PIECE: 'Stück', GRAMM: 'Gramm', KILOGRAMM: 'Kilogramm', LITRE: 'Liter', DECILITRE: 'Deziliter'}).map(([unit, label]) => (
                            <option key={unit} value={unit}>{label}</option>
                        ))}
                    </Form.Select>
                </Form.Group>
            </Col>
            <Col xs={12} sm>
                <Form.Group className="mb-1" controlId={`ingredientAmount-${ingredient.listId}`}>
                    <Form.Label>Menge</Form.Label>
                    <Form.Control name="amount" placeholder="z. B. 500" type="number" min="1" max="2147483647" step="1"
                        value={ingredient.amount} onChange={updateField} required />
                </Form.Group>
            </Col>
            <Col xs="auto">
                <Button type="button" variant="outline-dark" aria-label="Zutat entfernen"
                    onClick={() => removeIngredient(ingredient)}>Entfernen</Button>
            </Col>
        </Row>
    );
};

export default AddIngredient;
