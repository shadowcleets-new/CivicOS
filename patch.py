with open("gradlew", "r") as f:
    c = f.read()

c = c.replace("exit 1", "return 1")

with open("gradlew", "w") as f:
    f.write(c)
