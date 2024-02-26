#!/bin/bash

while [[ $# -gt 0 ]]; do
    case "$1" in
        -d|--server-cgi-dir)
            SERVER_CGI_DIR="$2"
            shift 2
            ;;
        -p|--production-file)
            shift
            while [[ $# -gt 0 ]]; do
                case "$1" in
                    -*)
                        break
                        ;;
                    *)
                        PRODUCTION_FILES+=("$1")
                        shift
                        ;;
                esac
            done
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done


if [ -z "$SERVER_CGI_DIR" ]; then
    if [ -f .env ]; then
        source .env
        if [ -n "$SERVER_CGI_DIR" ]; then
            echo "Using SERVER_CGI_DIR from .env: $SERVER_CGI_DIR"
        else
            echo "Error: SERVER_CGI_DIR is not defined in .env"
            exit 1
        fi
    else
        echo "Error: Missing required variable SERVER_CGI_DIR and .env file does not exist"
        exit 1
    fi
fi

if [ -z "$PRODUCTION_FILES" ]; then
    if [ -f .env ]; then
        source .env
        if [ -n "$PRODUCTION_FILES" ]; then
            echo "Using PRODUCTION_FILES from .env: $PRODUCTION_FILES"
        else
            echo "Error: PRODUCTION_FILES is not defined in .env"
            exit 1
        fi
    else
        echo "Error: Missing required variable PRODUCTION_FILES and .env file does not exist"
        exit 1
    fi
fi

for file in "${PRODUCTION_FILES[@]}"; do
    dir_path=$(dirname "$file")
    mkdir -p "$SERVER_CGI_DIR/$dir_path"
    if [ -d "$file" ]; then
        cp -r "$file" "$SERVER_CGI_DIR/$dir_path/"
    else
        cp "$file" "$SERVER_CGI_DIR/$dir_path/"
    fi
    chmod 755 "$SERVER_CGI_DIR/$dir_path/$(basename "$file")"
    echo "Copied $file to $SERVER_CGI_DIR/$dir_path/"
done