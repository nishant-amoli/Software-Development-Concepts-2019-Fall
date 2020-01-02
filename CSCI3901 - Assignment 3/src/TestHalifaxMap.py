from HalifaxMap import HalifaxMap

obj = HalifaxMap()
# print(obj.calculateDistance(4, 8, 12, 14))

obj.newIntersection(2, 3)
obj.newIntersection(5, 6)
obj.newIntersection(9, 0)
obj.newIntersection(0, 9)
obj.newIntersection(5, 1)
obj.newIntersection(2, 4)
obj.newIntersection(0, 1)
obj.newIntersection(5, 2)
obj.newIntersection(1, 8)
obj.newIntersection(5, 5)
obj.newIntersection(6, 6)
# obj.printCoordinates()

obj.defineRoad(1, 8, 5, 6)

obj.defineRoad(5, 1, 2, 4)
obj.defineRoad(0, 9, 5, 6)
obj.defineRoad(0, 9, 5, 1)

obj.defineRoad(0, 9, 0, 1)
obj.defineRoad(2, 4, 5, 2)
obj.defineRoad(5, 1, 0, 1)

# obj.printAdjacencyListIndices()
# obj.printAdjacencyListCoordinates()

obj.navigate(1, 8, 5, 1)
