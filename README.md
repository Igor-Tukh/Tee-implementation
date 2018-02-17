**Information:**

[wikipedia](https://en.wikipedia.org/wiki/Tee_(command))

**Build:**

`cd tee`

`./run.sh` or `gradle build`

**Usage:**
tee [ -a | --append] [ --help ] [ File ... ]

-a | --append = append to file

--help = show help and exit

File ... = one or more filenames

**Run:**

`cd build/libs/`

`java -jar tee-1.0-SNAPSHOT.jar ...`

For example:

`cal | java -jar tee-1.0-SNAPSHOT.jar`

