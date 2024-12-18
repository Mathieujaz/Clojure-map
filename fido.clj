(ns fido
  (:gen-class)
  (:require [assignment3.food :as food]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn DisplayMenu []
  (println "\n\n*** Let's Feed Fido ***")
  (println "-----------------------\n")
  (println "1. Display list of map files")
  (println "2. Display a map for Fido")
  (println "3. Exit")
  (print "\nEnter an option? ")
  (flush)
  (read-line))

(defn clearscreen []
  (print (str (char 27) "[2J"))
  (flush))

(defn option1 []
  (let [files (filter #(str/ends-with? (.getName %) ".txt")
                      (file-seq (io/file ".")))]
    (println "\nMap List:")
    (doseq [file files]
      (println "* " (.getName file)))
    (println "\nPress any key to continue")
    (flush)
    (read-line))
  (clearscreen))

(defn option2 []
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file-name (read-line)]
    (if (.exists (io/file file-name))
      (let [map-content (slurp file-name)
            map-lines (food/ValidateMap map-content)]
        (if map-lines
          (do
            (println "\nThis is Fido's challenge:")
            (doseq [line map-lines] (println line))
            (let [mapVector (vec (map vec map-lines))
                  path (food/findfood mapVector 0 0 #{})]
              (if path
                (let [newMap (food/updatemap mapVector path)]
                  (println "\nWoo Hoo - Fido found her food!\n")
                  (doseq [line newMap]
                    (println (apply str line))))
                (println "\nOh no - Fido could not find her food"))))
          (println "\nUnfortunately, this is not a valid food map for Fido")))
      (println "\nOops: specified file" file-name "does not exist")))
  (println "\nPress any key to continue")
  (flush)
  (read-line)
  (clearscreen))

(defn Options [option]
  (cond
    (= option "1") (option1)
    (= option "2") (option2)
    (= option "3") (println "\nGood Bye\n")
    :else (println "Invalid menu option")))

(defn menu []
  (loop []
    (let [option (str/trim (DisplayMenu))]
      (if (= option "3")
        (println "\nGood Bye\n")
        (do
          (Options option)
          (recur)))))

  (defn -main [& args]
    (menu))
)