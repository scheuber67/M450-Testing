const autocannon = require('autocannon');
const fs = require('node:fs');

async function main() {
  const results = [];
  for (const connections of [1, 20, 50]) {
    const result = await autocannon({
      url: 'http://localhost:8081/students', connections, duration: 10,
      pipelining: 1, timeout: 5
    });
    results.push(result);
    console.log(`${connections} Verbindungen: ${result.requests.average} Requests/s, ` +
      `p99 ${result.latency.p99} ms, ${result.errors} Fehler, ${result.non2xx} Nicht-2xx`);
  }
  fs.mkdirSync('results', { recursive: true });
  fs.writeFileSync('results/load.json', JSON.stringify(results, null, 2));
  if (results.some(r => r.errors || r.non2xx || !r.requests.total)) process.exitCode = 1;
}
main().catch(error => { console.error(error); process.exitCode = 1; });
