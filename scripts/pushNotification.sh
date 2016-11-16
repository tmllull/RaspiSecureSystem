#!/bin/bash

MSG="message=Message to send"
curl -s \
        --form-string "token=APPTOKEN" \
        --form-string "user=USERTOKEN" \
        --form-string "$MSG" \
        https://api.pushover.net/1/messages.json
exit 0
