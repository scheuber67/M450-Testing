import React from 'react';
import './AddIngredient.css';
import {Button, Col, Form, Row} from 'react-bootstrap';

const AddIngredient = ({ingredient, updateIngredient, removeIngredient}) => {
    const updateField = (event) => {
        updateIngredient({...ingredient, [event.target.name]: event.target.value});
    };

    return (
        <Row>
            <Col>
                <Form.Group className="mb-1" controlId={`ingredientName-${ingredient.listId}`}>
                    <Form.Label>Ingredient</Form.Label>
                    <Form.Control name="name" value={ingredient.name} onChange={updateField} required />
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1" controlId={`ingredientUnit-${ingredient.listId}`}>
                    <Form.Label>Unit</Form.Label>
                    <Form.Select name="unit" value={ingredient.unit} onChange={updateField}>
                        {['PIECE', 'GRAMM', 'KILOGRAMM', 'LITRE', 'DECILITRE'].map(unit => (
                            <option key={unit} value={unit}>{unit}</option>
                        ))}
                    </Form.Select>
                </Form.Group>
            </Col>
            <Col>
                <Form.Group className="mb-1" controlId={`ingredientAmount-${ingredient.listId}`}>
                    <Form.Label>Quantity</Form.Label>
                    <Form.Control name="amount" type="number" min="1" max="2147483647" step="1"
                        value={ingredient.amount} onChange={updateField} required />
                </Form.Group>
            </Col>
            <Col xs="auto">
                <Button type="button" variant="outline-dark" aria-label="Remove ingredient"
                    onClick={() => removeIngredient(ingredient)}>x</Button>
            </Col>
        </Row>
    );
};

export default AddIngredient;
