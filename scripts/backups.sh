# !/bin/sh

MSG="message=Backup completed"
MSG2="message=ERROR: Backup Failed"


sudo tar -cf "/home/pi/backups/backup_$(date '+%F').tar" /home /var/www/
rsync -ae ssh /home/pi/backups/* pi@192.168.1.13:/media/hdd/backups/pti > /dev/null 2>/dev/null
sudo rm /home/pi/backups/*

if [ "$?" -eq "0" ] || [ "$?" -eq "1" ];
then
  curl -s \
        --form-string "token=XXX" \
        --form-string "user=XXX" \
        --form-string "$MSG" \
        https://api.pushover.net/1/messages.json
else
  curl -s \
        --form-string "token=XXX" \
        --form-string "user=XXX" \
        --form-string "$MSG2" \
        https://api.pushover.net/1/messages.json
fi
