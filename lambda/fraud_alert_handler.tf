import json

def lambda_handler(event, context):
    for record in event["Records"]:
        body = json.loads(record["body"])
        print(" FRAUD ALERT ")
        print(f"Transaction: {body['transactionId']}")
        print(f"Reason: {body['reason']}")
        print(f"Score: {body['score']}")

    return {"status": "processed"}