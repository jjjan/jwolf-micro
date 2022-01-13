from pypmml import Model
model = Model.load("D:\mysoft\jwolf-micro\jwolf-bigdata\model\KMeansModelPMML.pmml")

predict1 = model.predict([1, 1, 1, 1, 5, 1, 1, 2, 2, 1])
predict2 = model.predict([2 ,5 ,5,5 ,5 ,5, 6, 6, 2, 1])
predict3 = model.predict([8 ,5 ,9,9 ,9 ,9, 9, 6, 8, 1])
print(predict1,predict2,predict3)
