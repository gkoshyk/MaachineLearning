# Load necessary packages

install.packages('curl', repos = 'http://cran.r-project.org')
devtools::install_github('systematicinvestor/SIT.date')
library(curl)
curl_download('https://github.com/systematicinvestor/SIT/raw/master/SIT.tar.gz', 'sit',mode = 'wb',quiet=T)
install.packages('sit', repos = NULL, type='source')
library(SIT)

# Question 2.a

load.packages('quantmod')
tickers = spl('DJIA,SPY,AAPL,BAC,NFLX,PCLN,AMZN')
data <- new.env()
getSymbols(tickers, src = 'yahoo', from = '2000-01-01', env = data, auto.assign = T)
bt.prep(data, align='keep.all', dates='2000::2016')

prices = data$prices

# Question 2.b

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


# Question 2.c
# I assigned all the stocks individually to the data environment and ran the below commands to generate the graph

# Buy & Hold    
data$weight[] = 1
buy.hold = bt.run(data) 

#SMA time period is 200 -------->
# MA Cross
sma = bt.apply(data, function(x) { SMA(Cl(x), 200) } )
data$weight[] = NA
data$weight[] = iif(prices >= sma, 1, 0)
sma.cross = bt.run(data, trade.summary=T)


# Create Report
plotbt.custom.report.part1(buy.hold, sma.cross)