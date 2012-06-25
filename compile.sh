if [ -f main-classes ]; then
    echo "main classes exists"
else 
    mkdir main-classes
fi
javac @options @classes
