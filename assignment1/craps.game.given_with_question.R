#*****THE CODE FOR MARTINGALES SYSTEM IS FROM LINE 44****

# Run the following line once to install the package random
# install.packages("random", dependencies = TRUE)
require(random)
craps <- function() {
  field <- c(2,3,12)
  wins <- c(7,11)
  initialRoll <- as.integer(colSums(randomNumbers(2, 1, 6, 1)))
  if (initialRoll %in% field)
    out <- 0
  else if (initialRoll %in% wins)
    out <- 1
  else {
    point <- initialRoll
    # now run the game until you get 7 or point again
    roll <- 0
    while(roll!= point && roll!=7) {
      roll <- as.integer(colSums(randomNumbers(2, 1, 6, 1)))
    }
    if (roll == point)
      out <- 1
    else if (roll == 7)
      out <- 0
    out
  }
  
}

#In craps game zero means lost and 1 means won.


# Give a hypothesis
# 
# Martingale hypothesis  or system
# That there is a system and you start off with 100$ bet and you have a total of 1000$
# If you win bet 100 
# If you loose bet the double of what you bet previously.
# If you loose your balance is you loose what you bet.
# Simulate this game given a 1000 dollars simulate the martingale system
# Write a for loop with starting bet as 100$


for(round in 1:10){
  balance <- 1000
  bet <- 100
  game <- 0
  while(balance>0 && game<10 ){
    game <- game+1
    x <- craps()
    if(x==0){
      balance <- balance - bet
      bet <- 2*bet
      if(bet>balance && game!=10){
        # If bet is greater than balance place the complete remaining amount as bet
        bet <- balance
      }
    }
    else{
      balance <- balance + bet
      bet <- 100
    }
  }
  cat("Round=",round," Number of Games = ",game, " Ending Amount=", balance,"\n\n")
}