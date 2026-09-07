from decimal import Decimal

def reconcile(records):
    mismatches=[]
    ledger_total=Decimal("0")
    processed_total=Decimal("0")
    for r in records:
        ledger_total += Decimal(str(r["ledger_amount"]))
        processed_total += Decimal(str(r["processed_amount"]))
        if Decimal(str(r["ledger_amount"])) != Decimal(str(r["processed_amount"])):
            mismatches.append(r["transaction_id"])
    return {"records_checked":len(records),"ledger_total":str(ledger_total),
            "processed_total":str(processed_total),"mismatches":mismatches}

if __name__=="__main__":
    data=[{"transaction_id":i,"ledger_amount":"100.00","processed_amount":"100.00"} for i in range(1,5001)]
    print(reconcile(data))
