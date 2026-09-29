const { test, expect } = require('@playwright/test');
const { randomUUID } = require('node:crypto');
const url = 'http://localhost:8081/students';

test('GET liefert Studenten als JSON', async ({ request }) => {
  const response = await request.get(url);
  expect(response.status()).toBe(200);
  expect(response.headers()['content-type']).toContain('application/json');
  const students = await response.json();
  expect(Array.isArray(students)).toBe(true);
  expect(students).toContainEqual(expect.objectContaining({ name: 'Jonas', email: 'jonas@tbz.ch' }));
});

test('POST speichert einen Studenten', async ({ request }) => {
  const student = { name: 'API Test', email: `${randomUUID()}@example.com` };
  const response = await request.post(url, { data: student });
  expect(response.status()).toBe(200);
  const list = await request.get(url);
  expect(list.status()).toBe(200);
  expect(await list.json()).toContainEqual(expect.objectContaining({ ...student, id: expect.any(Number) }));
});

test('Defektes JSON wird abgelehnt', async ({ request }) => {
  const response = await request.post(url, {
    headers: { 'Content-Type': 'application/json' }, data: '{ kaputt'
  });
  expect(response.status()).toBe(400);
});
