import math

"""
This class accepts the coordinates to add on the map and
also takes the current and destination coordinate points
to print the coordinates following the shortest path between them.
"""


class HalifaxMap:
    _setPermanent = 1
    _isTemporary = 0

    def __init__(self):
        self.cartesianSystem = []
        self.roadMapObj = {}
        self.path = []
        self.shortestPath = []

    # Class designed to store the coordinates into an object.
    class Coordinate:
        def __init__(self, x, y):
            self.x = x
            self.y = y

    # Class designed to store the coordinate names and the traversing cost into an object.
    class IndexCostNode:

        def __init__(self, index, cost):
            self.index = index
            self.cost = cost

    # Class designed to store the information of a point while creating the shortest path
    class Vertex:

        def __init__(self, destinationPoint, cost, routePoint, routeState):
            self.destinationPoint = destinationPoint
            self.cost = cost
            self.routePoint = routePoint
            self.routeState = routeState

    """
    Method that takes the coordinates from the user and store them
	into the Cartesian system.
    """

    def newIntersection(self, x, y):
        acknowledged = False
        try:
            # Check whether the coordinates already exist
            if self.checkCoordinates(x, y):
                print("Coordinates already exist in the system")

            else:
                self.cartesianSystem.append(self.Coordinate(x, y))
                acknowledged = True
        except:
            print("Exception while adding a new intersection")
        return acknowledged

    """
    Method that accepts the coordinates of two points and create an edge 
	between them.
    """

    def defineRoad(self, x1, y1, x2, y2):
        isNew = False
        try:
            if not self.cartesianSystem:
                print("System is empty")
            else:
                indexPointOne = self.getIndex(x1, y1)
                indexPointTwo = self.getIndex(x2, y2)

                # Check whether the points exist in Cartesian system
                if not (self.checkCoordinates(x1, y1) and self.checkCoordinates(x2, y2)):
                    print("Coordinates don't exist in the system")
                elif indexPointOne == indexPointTwo:
                    print("Cycles are not allowed")

                # Check whether the road has already been constructed
                elif self.doesRoadExist(x1, y1, x2, y2):
                    print("Road already defined")

                # Construct the road between the given points
                else:
                    # if both the points have been mapped before
                    if indexPointOne in self.roadMapObj and indexPointTwo in self.roadMapObj:
                        self.roadMapObj[indexPointOne].append(
                            self.IndexCostNode(indexPointTwo, self.calculateDistance(x1, y1, x2, y2)))
                        self.roadMapObj[indexPointTwo].append(
                            self.IndexCostNode(indexPointOne, self.calculateDistance(x1, y1, x2, y2)))

                    # if just indexPointOne has been mapped before
                    elif indexPointOne in self.roadMapObj:
                        self.roadMapObj[indexPointOne].append(
                            self.IndexCostNode(indexPointTwo, self.calculateDistance(x1, y1, x2, y2)))
                        self.roadMapObj[indexPointTwo] = []
                        self.roadMapObj[indexPointTwo].append(
                            self.IndexCostNode(indexPointOne, self.calculateDistance(x1, y1, x2, y2)))

                    # if just indexPointTwo has been mapped before
                    elif indexPointTwo in self.roadMapObj:
                        self.roadMapObj[indexPointTwo].append(
                            self.IndexCostNode(indexPointOne, self.calculateDistance(x1, y1, x2, y2)))
                        self.roadMapObj[indexPointOne] = []
                        self.roadMapObj[indexPointOne].append(
                            self.IndexCostNode(indexPointTwo, self.calculateDistance(x1, y1, x2, y2)))

                    # if none of the points have been mapped before
                    else:
                        self.roadMapObj[indexPointOne] = []
                        self.roadMapObj[indexPointOne].append(
                            self.IndexCostNode(indexPointTwo, self.calculateDistance(x1, y1, x2, y2)))
                        self.roadMapObj[indexPointTwo] = []
                        self.roadMapObj[indexPointTwo].append(
                            self.IndexCostNode(indexPointOne, self.calculateDistance(x1, y1, x2, y2)))
                    isNew = True
        except:
            print("Exception while defining the road between the coordinates")
        return isNew

    """
    Method that lets the user to input the coordinates of two points
	and print the shortest path between them.
    """

    def navigate(self, x1, y1, x2, y2):
        try:
            if not self.cartesianSystem:
                print("System is empty")

            else:
                # if the coordinates don't exist on the map
                if not (self.checkCoordinates(x1, y1) and self.checkCoordinates(x2, y2)):
                    print("Coordinates don't exist on the map")

                elif self.getIndex(x1, y1) == self.getIndex(x2, y2):
                    print("Cycles are not allowed")

                elif not self.roadMapObj:
                    print("No path")

                elif self.getIndex(x1, y1) not in self.roadMapObj:
                    print("No path")

                else:
                    pointOneIndex = self.getIndex(x1, y1)
                    pointTwoIndex = self.getIndex(x2, y2)
                    self.createPath(x1, y1)
                    for i in range(0, len(self.cartesianSystem) - 1):
                        lowestCostNode = self.getLowestCostNode()
                        if lowestCostNode is None:
                            break
                        self.modifyPath(lowestCostNode, pointOneIndex)
                    self.printShortestPath(x1, y1, x2, y2)
        except:
            print("Exception while navigating")

    """
    Method to calculate the distance between the two points.
    """

    def calculateDistance(self, x1, y1, x2, y2):
        distance = 0
        try:
            # Distance formula
            distance = math.sqrt(math.pow((x2 - x1), 2) + math.pow((y2 - y1), 2))
            distance = round(distance)
        except:
            print("Exception while calculating distance")
        return int(distance)

    """
    Method to check whether the coordinates exist inside the
	Cartesian system.
    """

    def checkCoordinates(self, x, y):
        doesExist = False
        try:
            if not self.cartesianSystem:
                pass
            else:
                for obj in self.cartesianSystem:
                    if obj.x == x and obj.y == y:
                        doesExist = True
        except:
            print("Exception in checkCoordinates")
        return doesExist

    """
    Method to print the coordinates added by the user.
    """

    def printCoordinates(self):
        try:
            if self.cartesianSystem:
                for obj in self.cartesianSystem:
                    print("", obj.x, ",", obj.y, ")\t")
        except:
            print("Exception in printCoordinates")

    """
    Method that takes the coordinates and returns their index
	in the list of Cartesian system.
    """

    def getIndex(self, x, y):
        index = -1
        try:
            countIndex = 0
            for obj in self.cartesianSystem:
                if obj.x == x and obj.y == y:
                    index = countIndex
                countIndex += 1
        except:
            print("Exception in getIndex")
        return index

    """
    Method that takes the index of the Cartesian system list and 
	returns an object containing the index point's coordinates.
    """

    def getCoordinates(self, index):
        countIndex = 0
        coordinateObj = None
        try:
            for obj in self.cartesianSystem:
                if index == countIndex:
                    coordinateObj = obj
                countIndex += 1
        except:
            print("Exception in getCoordinates")
        return coordinateObj

    """
    Method to print the adjacency list. The nodes will be in the form of 
	name of the points in the Cartesian system.
    """

    def printAdjacencyListIndices(self):
        try:
            if self.roadMapObj:
                for key in self.roadMapObj.keys():
                    print("\n", key, end=" :\t")
                    ls = self.roadMapObj[key]
                    for obj in ls:
                        print(obj.index, " Cost : ", obj.cost, end="\t")
        except:
            print("Exception in printAdjacencyListIndices")

    """
    Method to print the adjacency list. The nodes will be in the form of 
	coordinates of the points in the Cartesian system.
    """

    def printAdjacencyListCoordinates(self):
        try:
            if self.roadMapObj:
                for key in self.roadMapObj.keys():
                    ob = self.getCoordinates(key)
                    print("\n(", ob.x, ",", ob.y, ") :", end="\t")
                    obj = self.roadMapObj[key]
                    for i in obj:
                        ob = self.getCoordinates(i.index)
                        print("(", ob.x, ",", ob.y, ") Cost : ", i.cost, end="\t")
        except:
            print("Exception in printAdjacencyListCoordinates")

    """
    Method to check the existence of the edge between two Cartesian points.
    """
    def doesRoadExist(self, x1, y1, x2, y2):
        roadExists = False
        try:
            if self.roadMapObj:
                indexPointOne = self.getIndex(x1, y1)
                indexPointTwo = self.getIndex(x2, y2)
                if indexPointOne in self.roadMapObj:
                    obj = self.roadMapObj[indexPointOne]
                    if obj.index == indexPointTwo:
                        roadExists = True
        except:
            print("Exception in doesRoadExist")
        return roadExists

    """
    Method to create the shortest path (a part of 
	Djikstra's algorithm).
    """
    def createPath(self, x1, y1):
        try:
            startingIndex = self.getIndex(x1, y1)
            if not self.roadMapObj:
                print("Road not defined")
            elif not (startingIndex in self.roadMapObj):
                print("Road not defined")
            else:
                obj = self.roadMapObj[startingIndex]
                for i in obj:
                    self.path.append(self.Vertex(i.index, i.cost, startingIndex, HalifaxMap._isTemporary))
        except:
            print("Exception in createPath")

    """
    Method to modify the path in case a new shortest path has
	been discovered (a part of Djikstra's algorithm).
    """
    def modifyPath(self, lowestCostNode, startingIndex):
        try:
            obj = self.roadMapObj[lowestCostNode.destinationPoint]
            for i in obj:
                isPresent = 0
                for ob in self.path:
                    if ob.destinationPoint == i.index:
                        if ob.cost > (i.cost + lowestCostNode.cost):
                            ob.routePoint = lowestCostNode.destinationPoint
                            ob.cost = (i.cost + lowestCostNode.cost)
                        isPresent = 1
                        break
                if (isPresent == 0 and i.index != startingIndex):
                    self.path.append(self.Vertex(i.index, lowestCostNode.cost + i.cost, lowestCostNode.destinationPoint, HalifaxMap._isTemporary))
            lowestCostNode.routeState = HalifaxMap._setPermanent
        except:
            print("Exception in modifyPath")

    """
    Method to determine the lowest cost route (a part of 
	Djikstra's algorithm).
    """
    def getLowestCostNode(self):
        lowestCostNode = None
        minCost = 9999
        try:
            for obj in self.path:
                if obj.cost < minCost and obj.routeState == HalifaxMap._isTemporary:
                    lowestCostNode = obj
        except:
            print("Exception in getLowestCostNode")
        return lowestCostNode

    """
    Method to check whether the coordinates are present inside
	the shortest path.
    """
    def checkCoordinatesInPath(self, x, y):
        doesExist = False
        try:
            index = self.getIndex(x,y)
            for obj in self.path:
                if obj.destinationPoint == index:
                    doesExist = True
        except:
            print("Exception in checkCoordinatesInPath")
        return doesExist

    """
    The method called by Navigate method to print the short path
	discovered between the given two points.
    """
    def printShortestPath(self, x1, y1, x2, y2):
        try:
            doesPathExist = self.checkCoordinatesInPath(x2, y2)
            if doesPathExist is False:
                print("No path", end="\n")
            else:
                destinationIndex = self.getIndex(x2, y2)
                startingIndex = self.getIndex(x1, y1)
                for obj in self.path:
                    if obj.destinationPoint == destinationIndex:
                        iterator = obj
                stopIteration = 0
                while iterator.destinationPoint != startingIndex and stopIteration == 0:
                    for obj in self.path:
                        if obj.destinationPoint == iterator.routePoint:
                            self.shortestPath.append(self.getCoordinates(iterator.destinationPoint))
                            iterator = obj
                        if iterator.routePoint == startingIndex:
                            stopIteration = 1
                if iterator.destinationPoint != startingIndex:
                    self.shortestPath.append(self.getCoordinates(iterator.destinationPoint))
                self.shortestPath.append(self.getCoordinates(startingIndex))
                print("\nShortest path:\n")
                index = len(self.shortestPath) - 1
                while index >= 0:
                    obj = self.shortestPath[index]
                    print(obj.x, "\t", obj.y)
                    index -= 1
        except:
            print("Exception in printShortestPath")