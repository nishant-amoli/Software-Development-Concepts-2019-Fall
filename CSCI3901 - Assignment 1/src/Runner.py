# -*- coding: utf-8 -*-
"""
Created on Sun Dec 15 14:45:42 2019

@author: Nishant
"""

from DataTransformer import DataTransformer
obj = DataTransformer()
obj.read("C:\\Users\\Nishant\\Downloads\\sample input.txt")
obj.newColumn("value")
obj.newColumn("tax")
obj.newColumn("total")
obj.calculate("value = baseprice * quantity")
obj.calculate("tax = value * 15")
obj.calculate("total = value + tax")
obj.print()
