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
//        shapeLayer.strokeStart = 0.35
        
        
        let roundCenter = CGPoint.init(x: 100, y: 300)
        shapeLayer.path = UIBezierPath.init(arcCenter: roundCenter, radius: 50, startAngle: 180, endAngle: 90, clockwise: true).cgPath
//        shapeLayer.frame = CGRect.init(x: 50, y: 250, width: 50, height: 50)
        shapeLayer.bounds = CGRect.init(x: 100, y:300,width: 50, height: 50)
        shapeLayer.position = CGPoint.init(x:100, y: 300)
        shapeLayer.anchorPoint = CGPoint.init(x:0, y:0)
        
        let shapeAnimation = CABasicAnimation.init()
        shapeAnimation.duration = 10
        shapeAnimation.keyPath = "transform.rotation"
        shapeAnimation.fromValue = 0
        shapeAnimation.toValue = 45
        shapeAnimation.repeatCount = 1

        
        shapeLayer.add(shapeAnimation, forKey: nil)

        
        view.layer.addSublayer(layer)
        view.layer.addSublayer(shapeLayer)
        
        
        
        
        
    }
    
}
