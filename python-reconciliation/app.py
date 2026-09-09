from fastapi import FastAPI
from pydantic import BaseModel
from decimal import Decimal

app=FastAPI(title="Reconciliation Service")

class Record(BaseModel):
    transaction_id:int
    ledger_amount:Decimal
    processed_amount:Decimal

@app.get("/health")
def health(): return {"service":"reconciliation","status":"ok"}

@app.post("/reconcile")
def reconcile(records:list[Record]):
    mismatches=[r.transaction_id for r in records if r.ledger_amount != r.processed_amount]
    return {"records_checked":len(records),"mismatches":len(mismatches),"transaction_ids":mismatches[:100]}

@app.get("/benchmark/5000")
def benchmark():
    records=[Record(transaction_id=i,ledger_amount=Decimal("100.00"),processed_amount=Decimal("100.00")) for i in range(1,5001)]
    result=reconcile(records)
    return {"benchmark":"5000 simulated records","result":result}
