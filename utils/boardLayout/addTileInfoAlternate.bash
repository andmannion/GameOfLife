# Use this if it is required to delete the default xLocation and yLocation
# fields added to the board .json config by Conor
sed -i '/\"gameBoardTile\": {/{n;N;d}' GameBoardConfig.json
