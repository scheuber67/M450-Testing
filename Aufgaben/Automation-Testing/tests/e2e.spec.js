const { test, expect } = require('@playwright/test');
const { randomUUID } = require('node:crypto');

test('Student im Browser erfassen und nach Neuladen wiederfinden', async ({ page }) => {
  const email = `${randomUUID()}@example.com`;
  await page.goto('/students');
  await page.getByRole('link', { name: 'Add Students' }).click();
  await page.getByLabel('Name', { exact: true }).fill('Browser Test');
  await page.getByLabel('Email', { exact: true }).fill(email);
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page).toHaveURL(/\/students$/);
  await page.reload();
  const row = page.getByRole('row').filter({ hasText: email });
  await expect(row).toContainText('Browser Test');
  await expect(row).toContainText(email);
});
