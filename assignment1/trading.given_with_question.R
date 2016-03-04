# Install Systematic Investor first
# Run only once
install.packages('curl', repos = 'http://cran.r-project.org')
###############################################################################
# Install Systematic Investor Toolbox (SIT) package
# github.com/systematicinvestor/SIT
###############################################################################

#Install devtools package first
#Go to right hand side packages and click the check box
# please first install SIT.date
devtools::install_github('systematicinvestor/SIT.date')

library(curl)
curl_download('https://github.com/systematicinvestor/SIT/raw/master/SIT.tar.gz', 'sit',mode = 'wb',quiet=T)
install.packages('sit', repos = NULL, type='source')
library(SIT)
##########
# test strategies
##########

#*****************************************************************
# Load historical data
#******************************************************************     
load.packages('quantmod')

#Looks like spl function comes from one of the packages above
tickers = spl('DJIA,SPY,AAPL,BAC,NFLX,PCLN,AMZN')

# 'DJIA,SPY,AAPL,BAC,NFLX,PCLN,AMZN'

data <- new.env()
getSymbols(tickers, src = 'yahoo', from = '2000-01-01', env = data, auto.assign = T)
bt.prep(data, align='keep.all', dates='2000::2016')

#*****************************************************************
# Code Strategies
#****************************************************************** 
prices = data$prices

chartSeries(prices$DJIA)
addSMA(n=200,on=1) #on is to superimpose on a preexisting chart
chartSeries(prices$SPY)
addSMA(n=200,on=1)
chartSeries(prices$AAPL)
addSMA(n=200,on=1)
chartSeries(prices$BAC)
addSMA(n=200,on=1)
chartSeries(prices$NFLX)
addSMA(n=200,on=1)
chartSeries(prices$PCLN)
addSMA(n=200,on=1)
chartSeries(prices$AMZN)
addSMA(n=200,on=1)

# Buy & Hold    
data$weight[] = 1
buy.hold = bt.run(data) 

#SMA time period is 200 -------->
# MA Cross
sma = bt.apply(data, function(x) { SMA(Cl(x), 200) } )
data$weight[] = NA
data$weight[] = iif(prices >= sma, 1, 0)
sma.cross = bt.run(data, trade.summary=T)

#*****************************************************************
# Create Report
#****************************************************************** 
plotbt.custom.report.part1(buy.hold, sma.cross)
#plotbt.custom.report.part2(buy.hold, sma.cross)
#plotbt.custom.report.part3(buy.hold, sma.cross)
rm(data)



#SMA is simple moving average
#help of any commmand  
library(quantmod)
?quantmod::weeklyReturn