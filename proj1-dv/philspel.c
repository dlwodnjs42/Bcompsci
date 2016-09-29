/*
 * Include the provided hashtable library
 */
#include "hashtable.h"

/*
 * Include the header file
 */
#include "philspel.h"

/*
 * Standard IO and file routines
 */
#include <stdio.h>

/*
 * General utility routines (including malloc())
 */
#include <stdlib.h>

/*
 * Character utility routines.
 */
#include <ctype.h>

/*
 * String utility routines
 */
#include <string.h>

/*
 * this hashtable stores the dictionary
 */
HashTable *dictionary;

/*
 * the MAIN routine.  You can safely print debugging information
 * to standard error (stderr) and it will be ignored in the grading
 * process, in the same way which this does.
 */
int main(int argc, char **argv){
  if(argc != 2){
    fprintf(stderr, "Specify a dictionary\n");
    return 0;
  }
  /*
   * Allocate a hash table to store the dictionary
   */
  fprintf(stderr, "Creating hashtable\n");
  dictionary = createHashTable(2255, &stringHash, &stringEquals);

  fprintf(stderr, "Loading dictionary %s\n", argv[1]);
  readDictionary(argv[1]);
  fprintf(stderr, "Dictionary loaded\n");

  fprintf(stderr, "Processing stdin\n");
  processInput();

  /* main in C should always return 0 as a way of telling
     whatever program invoked this that everything went OK
     */
  return 0;
}

/*
 * You need to define this function. void *s can be safely casted
 * to a char * (null terminated string) which is done for you here for
 * convenience.
 */
unsigned int stringHash(void *s){
  int total = 33;
  char *string = (char *) s;
  int x;

  while((x = *string++)) {
    total = (((total << 5) + total) + x);
  }
  return total;
}

/*
 * You need to define this function.  It should return a nonzero
 * value if the two strings are identical (case sensitive comparison)
 * and 0 otherwise.
 */
int stringEquals(void *s1, void *s2){
  char *string1 = (char *) s1;
  char *string2 = (char *) s2;
  if (stringHash(string1) == stringHash(string2)) {
    return 1;
  }
  return 0;
}

/*
 * this function should read in every word in the dictionary and
 * store it in the dictionary.  You should first open the file specified,
 * then read the words one at a time and insert them into the dictionary.
 * Once the file is read in completely, exit.  You will need to allocate
 * (using malloc()) space for each word.  As described in the specs, you
 * can initially assume that no word is longer than 60 characters.  However,
 * for the final 20% of your grade, you cannot assumed that words have a bounded
 * length You can NOT assume that the specified file exists.  If the file does
 * NOT exist, you should print some message to standard error and call exit(0)
 * to cleanly exit the program. Since the format is one word at a time, with
 * returns in between, you can
 * safely use fscanf() to read in the strings.
 */
void readDictionary(char *filename){

  FILE *fp = NULL;
  char *string = (char *)malloc(60); // free up 60 space maybe use sizeof later
  int count = 0; // used to traverse
  char car;
  int buffer = 60;


  fp = fopen(filename, "r"); //open it up
  if (fp == NULL) {  //does the specified file exist?
    fprintf(stderr, "File does not exist");
    exit(0);
  } else {

    while ((car = (char) fgetc(fp)) != EOF) {
      string[count] = car;
      count++;
      while ((car = (char) fgetc(fp)) != '\n') {
        if (count >= buffer) {
          string = (char *) realloc(string, buffer * 2);
          buffer = buffer * 2;  
          string[count] = car;
        }
        else {
          string[count] = car;
          
        }
        count++; 
      }
      string[count] = '\0';
      insertData(dictionary, (void *)string, (void *)string);
      if (car ==EOF ) {
        fclose(fp);
        exit(0);
      }
      string = (char *)malloc(60);
      count = 0;
    }
    // for (; *string != 0; string ++) {
    //   printf("%c", *string);
    // }
    //how do i make a new string every time 
    //how do i compare strings
  }
  fclose(fp);
}


/*
 * This should process standard input and copy it to standard output
 * as specified in specs.  EG, if a standard dictionary was used
 * and the string "this is a taest of  this-proGram" was given to
 * standard input, the output to standard output (stdout) should be
 * "this is a teast [sic] of  this-proGram".  All words should be checked
 * against the dictionary as they are input, again with all but the first
 * letter converted to lowercase, and finally with all letters converted
 * to lowercase.  Only if all 3 cases are not in the dictionary should it
 * be reported as not being found, by appending " [sic]" after the
 * error.
 *
 * Since we care about preserving whitespace, and pass on all non alphabet
 * characters untouched, and with all non alphabet characters acting as
 * word breaks, scanf() is probably insufficent (since it only considers
 * whitespace as breaking strings), so you will probably have
 * to get characters from standard input one at a time.
 *
 * As stated in the specs, you can initially assume that no word is longer than
 * 60 characters, but you may have strings of non-alphabetic characters (eg,
 * numbers, punctuation) which are longer than 60 characters. For the final 20%
 * of your grade, you can no longer assume words have a bounded length.
 */
void processInput(){



  char *string = (char *) malloc(60); // free up 60 space maybe use sizeof later
  int count = 0; // used to traverse
  char parser;
  int buffer = 60;
  char *stringextra;
  char *stringextra2;
  int i = 0;
  int t = 1;


  while ((parser = fgetc(stdin)) != EOF) {
    if (!isalpha(parser)) {
      fprintf(stdout, "%c", parser);
    } else {
      string[count] = parser;
      count ++;
      while (isalpha(parser = fgetc(stdin)) != 0 && parser != '\n' && parser != EOF) {//isalpha
          if (count >= buffer) {
            string = (char *) realloc(string, buffer * 2);
            buffer = buffer * 2;   //reallocate with +1 word
            
          }
          else {
            string[count] = (char) parser;
          }
          count++;
        }
        char x = parser;
        string[count] = '\0';
        stringextra = (char *)malloc(sizeof(string));
        stringextra2 = (char *)malloc(sizeof(string));

        stringextra = strdup(string);
        stringextra2 = strdup(string);
        // fprintf(stderr, "%s", stringextra);
        // fprintf(stderr, "%s\n", stringextra2);

        for (; t < sizeof(stringextra2) ; t++) { //first one normal
          stringextra2[t] = (char) tolower(stringextra2[t]);
        }
        for (; i < sizeof(stringextra) ; i++) { // all lower case
          stringextra[i] =  (char) tolower(stringextra[i]);
        }
        stringextra[count] = '\0';
        stringextra2[count] = '\0'; 
        // fprintf(stderr, "%s", stringextra2);
        // fprintf(stderr, "%s\n", stringextra);
        if (!findData(dictionary, string) && !findData(dictionary,stringextra) && !findData(dictionary,stringextra2)) {
          fprintf(stdout, "%s", string);
          fprintf(stdout, " [sic]");
        } else {
          fprintf(stdout, "%s", string);
        }
        if (parser == EOF) {
          exit(0);
        }
        fprintf(stdout, "%c", x);
        



          // free(string);
          // free(stringextra);
          // free(stringextra2);
        
        count = 0;
        t = 1;
        i = 0;
      }
    }
    fprintf(stdout, "\n");
    free(string);
    free(stringextra);
    free(stringextra2);
  }




  
