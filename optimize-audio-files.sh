#!/bin/bash

SRC_DIR="assets/raw-audio"
DEST_DIR="assets/audio"

shopt -s nullglob

for file in "$SRC_DIR"/*; do
    filename=$(basename "$file")
    base="${filename%.*}"
    extension="${filename##*.}"
    output="$DEST_DIR/${base}.wav"

    if [[ -f "$output" ]]; then
        echo "Skipping existing: $output"
        continue
    fi

    case "$(echo "$extension" | tr '[:upper:]' '[:lower:]')" in
        wav)
            echo "Converting WAV to PCM: $filename"
            ffmpeg -y -hide_banner -loglevel error \
              -i "$file" -acodec pcm_s16le -ac 1 -ar 44100 "$output"
            ;;
        mp3)
            echo "Converting MP3 to WAV: $filename"
            ffmpeg -y -hide_banner -loglevel error \
              -i "$file" -acodec pcm_s16le -ac 1 -ar 44100 "$output"
            ;;
        *)
            echo "Skipping unsupported file: $filename"
            ;;
    esac
done
