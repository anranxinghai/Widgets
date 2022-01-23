//
//  UILayerViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/10.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import QuartzCore
import UIKit
class UILayerViewController:UIViewController{
    private var uiLabel:UILabel?
    override func viewDidLoad() {
        
        super.viewDidLoad()
        view.backgroundColor = .white
        let layer = CALayer.init()
        layer.frame = CGRect.init(x: 100, y: 100, width: 200, height: 100)
        layer.backgroundColor = UIColor.red.cgColor
        
        
        let shapeLayer = CAShapeLayer.init()
        shapeLayer.strokeColor = UIColor.green.cgColor
        shapeLayer.fillColor = UIColor.clear.cgColor
        shapeLayer.lineWidth = 2
        shapeLayer.strokeStart = 0.35
        
//        shapeLayer.backgroundColor = UIColor.cyan.cgColor
        
        
        let roundCenter = CGPoint.init(x: 100, y: 300)
        shapeLayer.path = UIBezierPath.init(arcCenter: roundCenter, radius: 50, startAngle: 0, endAngle: 2*CGFloat.pi, clockwise: true).cgPath
//        shapeLayer.frame = CGRect.init(x: 100, y: 300, width: 100, height: 100)
        shapeLayer.bounds = CGRect.init(x: 50, y: 250, width: 100, height: 100)
//        shapeLayer.position = CGPoint.init(x: 50, y: 50)
//        shapeLayer.anchorPoint = CGPoint.init(x: 1, y: 1)
        
        
        let shapeAnimation = CABasicAnimation.init()
        shapeAnimation.duration = 10
        shapeAnimation.keyPath = "transform.rotation"
        shapeAnimation.fromValue = 0
        shapeAnimation.toValue = 45
        shapeAnimation.repeatCount = 1


        shapeLayer.add(shapeAnimation, forKey: nil)

        
        view.layer.addSublayer(layer)
        view.layer.addSublayer(shapeLayer)
        
        print("position:\(shapeLayer.position)")
        print("frame:\(shapeLayer.frame)")
        print("bounds:\(shapeLayer.bounds)")
        print("anchorPoint:\(shapeLayer.anchorPoint)")
        
//        shapeLayer.transform = CATransform3DMakeRotation(45, 0, 0, 1)
        
        
    }
    
}
