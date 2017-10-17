java -jar target/monomono.jar -g samples/gen5k_JPY.clj -o output/gen5k -p -c -r `pwd`/resources

ls -al output/gen5k | wc -l
