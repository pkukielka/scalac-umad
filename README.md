# scala-umad

Tool for quick and simple testing changes for parallelizing Scala.

Setup:

```
cd <WORKSPACE>
git clone https://github.com/pkukielka/scala-umad.git
git clone -b 2.13.x-parallelize https://github.com/rorygraves/scalac_perf.git

cd <WORKSPACE>/scalac_perf
sbt dist/mkPack
```

Usage:

On Unix
```
cd <WORKSPACE>/scala-umad
./run.py -s ../scalac_perf/build/pack/ -- -Yparallel-phases:parser,refchecks,patmat -Yparallel-sequential
```
On Windows
```
cd <WORKSPACE>\scala-umad
.\run.py -s ..\scalac_perf\build\pack\ -- -Yparallel-phases:parser,refchecks,patmat -Yparallel-sequential
```

Umad parameters are before `--`, jvm (scalac) parameters are after. 

`-Yparallel-phases` - allows to specify phases you want to run in parallel  
`-Yparallel-sequential` - causes all units to be run in separate threads one after another (sequentally);
this does not provide any real paralleism but ensure all violation will be detected by umad without being affected by races

Some of umad parameters can be changed in the config:
https://github.com/pkukielka/scala-umad/blob/master/umad/src/main/resources/application.conf

Also `./run.py -h` gives information about additional possible parameters.
