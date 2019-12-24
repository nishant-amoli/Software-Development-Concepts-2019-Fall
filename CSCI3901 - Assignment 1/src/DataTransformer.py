# -*- coding: utf-8 -*-
"""
Created on Sat Dec 14 22:55:32 2019

@author: Nishant
"""
import os.path
class DataTransformer:
    
    def __init__(self):
        self.columnNameList = []
        self.equationDictionary = []
        self.dictObj = {}
    
    '''
    Method to clear the data from the object.
    Returns true in case the object is empty after the operation.	 
    '''
    def clear(self):
        isObjectEmpty = False;
        try:
            if not self.dictObj:
                print("Object is empty")
            
            if self.dictObj:
                self.dictObj.clear()
                isObjectEmpty = True
            
        except:
            print("Exception while invoking clear method")
        return isObjectEmpty
    
    '''
	Method to read in the contents of the file to the object. Returns the 
	number of the data rows.
	'''
    def read(self, filename):
        rowCount = 0
        try:
            if filename.isspace() or filename == "":
                print("Blank filename")
                
            elif filename == None:
                print("Null filename")
                
            else:
                if os.path.isfile(filename): 
                    
                    if os.stat(filename).st_size == 0:
                        print("File is blank")
                        
                    else:
                        with open(filename, 'r') as fileObj:
                            rowsList = fileObj.read().splitlines()
                            rowCount = len(rowsList)-1
                            self.columnNameList = rowsList[0].split('\t')
                            for i in range(len(rowsList)):
                                self.dictObj[i] = rowsList[i].split('\t')
                                
                            fileObj.close()                    
                else:
                    print("File not found")
        except:
            print("Exception while reading from file")
        return rowCount
    
    '''
    The following method creates a new column with the given name. 
	Returns True if the new column is successfully created.
	'''
    def newColumn(self, columnName):
        flag = False
        try:            
            if columnName == None:
                print("Null column name")
                
            elif columnName.isspace() or columnName == "":
                print("Blank column name")  
                
            else:
                if columnName.casefold() in (name.casefold() for name in  self.columnNameList):
                    print("Column exists")
                    
                else:
                    self.dictObj[0].append(columnName)
                    self.columnNameList.append(columnName)
                    for i in range(1,len(self.dictObj)):
                        self.dictObj[i].append("0")
                    flag = True            
        except:
            print("Exception while adding new column")
            return flag
        
    '''
    The following methods take in an equation and apply it to the data in the object.
	Returns the number of rows that were calculated for the received equation.
	'''
    def calculate(self, equation):
        calculatedRowCount = 0
        try:
            self.equationDictionary = equation.split(" ")
            derivedColumnIndex = operandColumnIndex1 = int(-1)
            operandColumnIndex2 = index = int(-1)
            derivedColumnValue = operandColumnValue1 = float(-1)
            operandColumnValue2 = float(-1)
            for x in self.columnNameList:
                index = index + 1
                if x.lower() == self.equationDictionary[0].lower():
                    derivedColumnIndex = index 
                if x.lower() == self.equationDictionary[2].lower():
                    operandColumnIndex1 = index                
                if x.lower() == self.equationDictionary[4].lower():
                    operandColumnIndex2 = index
                                    
            for key in range(1,len(self.dictObj)):
                index = -1
                for x in self.dictObj[key]:
                    index = index + 1
                    if index == 0:
                        continue
                    if index == operandColumnIndex1:
                        operandColumnValue1 = float(x)
                    if index == operandColumnIndex2:
                        operandColumnValue2 = float(x)
                        
                if operandColumnIndex1 == -1:
                    operandColumnValue1 = float(self.equationDictionary[2])
                if operandColumnIndex2 == -1:
                    operandColumnValue2 = float(self.equationDictionary[4])
                
                if self.equationDictionary[3] == "+":
                    derivedColumnValue = operandColumnValue1 + operandColumnValue2
                if self.equationDictionary[3] == "-":
                    derivedColumnValue = operandColumnValue1 - operandColumnValue2
                if self.equationDictionary[3] == "*":
                    derivedColumnValue = operandColumnValue1 * operandColumnValue2
                if self.equationDictionary[3] == "/":
                    derivedColumnValue = operandColumnValue1 / operandColumnValue2
                index = -1
                        
                ls = self.dictObj[key]
                for x in ls:
                    index = index + 1
                    if index == derivedColumnIndex:
                        derivedColumnValue = round(derivedColumnValue)
                        temp = int(derivedColumnValue)
                        ls[index] = temp
                        self.dictObj[key] = ls
                        calculatedRowCount += 1            
        except:
            print("Exception while calculating the column's value")
        return calculatedRowCount
    
    '''
    Method to print the first 5 rows of the object.
    '''
    def top(self):
        try:
            if not self.dictObj:
                print("Object is empty")
            else:
                if len(self.dictObj) >= 5:
                    for i in range(5):
                        for j in self.dictObj[i]:
                            print(j,end = "\t")
                        print("\n")
                else:
                    for i in range(len(self.dictObj)):
                        for j in self.dictObj[i]:
                            print(j,end = "\t")
                        print("\n")
        except:
            print("Exception while invoking top method")
    
    '''
    Method to print all rows to the screen.
    '''
    def print(self):
        try:
            if not self.dictObj:
                print("Object is empty")
            else:
                for i in range(len(self.dictObj)):
                    for j in self.dictObj[i]:
                            print(j,end = "\t")
                    print("\n")
        except:
            print("Exception while printing on console")
            
    '''
    The following method writes the contents of the object to the given file.
    '''
    def write(self,fileName):
        dataRowCount = 0
        try:
            if not self.dictObj:
                print("Object is empty")
            else:
                with open(fileName,"w") as fileObj:
                    for i in range(len(self.dictObj)):
                        for j in self.dictObj[i]:
                            fileObj.write("%s\t"%j)
                        dataRowCount += 1
                        fileObj.write("\n")
                    fileObj.close()               
        except:
            print("Exception while writing on the file")
        return dataRowCount