//
//  UIParentViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/11.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import UIKit
class UIControlViewController:UIViewController{
    private var controllView:UIControllerView!
    var childView:ChildView!
    
    private var childView2:UIControllerView!
    var controllView2:ChildView!
    
    private var switchView:UISwitch!
    var childView3:ChildView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        navigationItem.title = "UIControlView"
        
        
        controllView = UIControllerView.init(frame: CGRect.init(x: 100, y: 100, width: 100, height: 100))
        controllView.layer.backgroundColor = UIColor.blue.cgColor
        view.addSubview(controllView)
        
        controllView.addTarget(self, action: #selector(controlTapInside(control:) ),for:.touchUpInside)
        
        childView = ChildView.init(frame: CGRect.init(x:0,y:0,width:50,height:50))
        
        childView.layer.backgroundColor = UIColor.red.cgColor
        controllView.addSubview(childView)
        
        childView.translatesAutoresizingMaskIntoConstraints = false
        
        childView.centerXAnchor.constraint(equalTo: controllView.centerXAnchor).isActive = true
        childView.centerYAnchor.constraint(equalTo: controllView.centerYAnchor).isActive = true
        childView.widthAnchor.constraint(equalToConstant: 50).isActive = true
        childView.heightAnchor.constraint(equalToConstant: 50).isActive = true
        
        
        
        childView2 = UIControllerView.init(frame: CGRect.init(x: 0, y:0, width: 50, height: 50))
        childView2.layer.backgroundColor = UIColor.blue.cgColor
        
        childView2.addTarget(self, action: #selector(controlTapInside(control:) ),for:.touchUpInside)
        
        controllView2 = ChildView.init(frame: CGRect.init(x:100,y:250,width:100,height:100))
        
        controllView2.layer.backgroundColor = UIColor.red.cgColor
        view.addSubview(controllView2)
        controllView2.addSubview(childView2)
        
        childView2.translatesAutoresizingMaskIntoConstraints = false
        
        childView2.centerXAnchor.constraint(equalTo: controllView2.centerXAnchor).isActive = true
        childView2.centerYAnchor.constraint(equalTo: controllView2.centerYAnchor).isActive = true
        childView2.widthAnchor.constraint(equalToConstant: 50).isActive = true
        childView2.heightAnchor.constraint(equalToConstant: 50).isActive = true
        
    
        
        switchView = UISwitch.init(frame: CGRect.init(x: 0, y: 0, width: 50, height: 50))
        switchView.layer.backgroundColor = UIColor.blue.cgColor
        
        
        switchView.addTarget(self, action: #selector(controlTapInside(control:) ),for:.touchUpInside)
        
        childView3 = ChildView.init(frame: CGRect.init(x:100,y:400,width:100,height:100))
        
        childView3.layer.backgroundColor = UIColor.red.cgColor
        childView3.addSubview(switchView)
        view.addSubview(childView3)
        switchView.translatesAutoresizingMaskIntoConstraints = false
        
        switchView.centerXAnchor.constraint(equalTo: childView3.centerXAnchor).isActive = true
        switchView.centerYAnchor.constraint(equalTo: childView3.centerYAnchor).isActive = true
        switchView.widthAnchor.constraint(equalToConstant: 50).isActive = true
        switchView.heightAnchor.constraint(equalToConstant: 50).isActive = true
        
        
        
    }
    
    
    class UIControllerView:UIControl{
        
        var childView:ChildView!
        override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesBegan(touches, with: event)
            print("UIControl view touchesBegan")
      
        }
        
        override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesMoved(touches, with: event)
            print("UIControl view touchesMove")
        }
        
        override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesEnded(touches, with: event)
            print("UIControl view touchesEnded")
        }
        
        override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesCancelled(touches, with: event)
            print("UIControl view touchesBegan")
        }
        
        override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
            var view = super.hitTest(point, with: event)
        
            return view
        }
        
        override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
            
            
            
            let viewFrame = self.frame
            let viewBounds = self.bounds
            let viewCenter = self.center
            
            let layerPosition = self.layer.position
            let layerFrame = self.layer.frame
            let layerBounds = self.layer.bounds
            let layerAnchorPoint = self.layer.anchorPoint
            if viewBounds.contains(point) {
                return true
            }
            return super.point(inside: point, with: event)
        }
    }
    
    class ChildView:UIControl{
        
        override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesBegan(touches, with: event)
            print("UIView view touchesBegan")
        }
        
        override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesMoved(touches, with: event)
            print("UIView view touchesMove")
        }
        
        override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesEnded(touches, with: event)
            print("UIView view touchesEnded")
        }
        
        override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesCancelled(touches, with: event)
            print("UIView view touchesBegan")
        }
        
        override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
            super.hitTest(point, with: event)
        }
        
        override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
        
            return super.point(inside: point, with: event)
        }
    }
}

private extension UIControlViewController{
    @objc func controlTapInside(control:UIControl){
        print("UIControl controlTapInside \(control)")
    }
}
