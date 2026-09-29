const autocannon = require('autocannon');
const fs = require('node:fs');

async function main() {
  const results = [];
  for (const connections of [1, 20, 50]) {
    // Gleiche Fehler zusammenfassen statt hunderte Zeilen auszugeben.
    const errorDetails = [];
    const result = await new Promise((resolve, reject) => {
      const run = autocannon({
        url: 'http://localhost:8081/students', connections, duration: 10,
        pipelining: 1, timeout: 5
      }, (error, result) => error ? reject(error) : resolve(result));
      run.on('reqError', error => {
        const code = error.code || 'UNKNOWN';
        const message = error.message || String(error);
        const existing = errorDetails.find(item => item.code === code && item.message === message);
        if (existing) existing.count++;
        else errorDetails.push({ code, message, count: 1 });
      });
    });
    result.errorDetails = errorDetails;
    results.push(result);
    console.log(`${connections} Verbindungen: ${result.requests.average} Requests/s, ` +
      `p99 ${result.latency.p99} ms, ${result.errors} Fehler, ${result.non2xx} Nicht-2xx`);
    for (const error of errorDetails) {
      console.log(`  ${error.count}x ${error.code}: ${error.message}`);
    }
  }
  fs.mkdirSync('results', { recursive: true });
  fs.writeFileSync('results/load.json', JSON.stringify(results, null, 2));
  if (results.some(r => r.errors || r.non2xx || !r.requests.total)) process.exitCode = 1;
}
main().catch(error => { console.error(error); process.exitCode = 1; });
