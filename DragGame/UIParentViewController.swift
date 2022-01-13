//
//  UIParentViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/11.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import UIKit
class UIParentViewController:UIViewController{
    private var parentView:ParentView!
    var childView:ChildView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        navigationItem.title = "ChildView"
        
        
        parentView = ParentView.init()
        parentView.layer.backgroundColor = UIColor.blue.cgColor
        view.addSubview(parentView)
        
        
        parentView.translatesAutoresizingMaskIntoConstraints = false
        parentView.leftAnchor.constraint(equalTo: view.leftAnchor,constant: 100).isActive = true
        parentView.topAnchor.constraint(equalTo: view.topAnchor,constant:  200).isActive = true
        
        parentView.widthAnchor.constraint(equalToConstant: 100).isActive = true
        parentView.heightAnchor.constraint(equalToConstant: 200).isActive = true
        
        childView = ChildView.init()
        
        childView.layer.backgroundColor = UIColor.red.cgColor
        parentView.addSubview(childView)
        
        childView.translatesAutoresizingMaskIntoConstraints = false
        
        childView.leadingAnchor.constraint(equalTo: parentView.centerXAnchor).isActive = true
        childView.topAnchor.constraint(equalTo: parentView.centerYAnchor).isActive = true
        childView.widthAnchor.constraint(equalToConstant: 100).isActive = true
        childView.heightAnchor.constraint(equalToConstant: 200).isActive = true
        parentView.childView = childView
        
    }
    
    
    class ParentView:UIView{
        
        var childView:ChildView!
        override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesBegan(touches, with: event)
            print("Parent view touchesBegan")
            
            let position = self.layer.position
            let frame = self.layer.frame
            let bounds = self.layer.bounds
            
        }
        
        override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesMoved(touches, with: event)
            print("Parent view touchesMove")
        }
        
        override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesEnded(touches, with: event)
            print("Parent view touchesEnded")
        }
        
        override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesCancelled(touches, with: event)
            print("Parent view touchesBegan")
        }
        
        override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
            var view = super.hitTest(point, with: event)
            
            let viewFrame = view?.frame
            let viewBounds = view?.bounds
            let viewCenter = view?.center
            
            let layerPosition = self.layer.position
            let layerFrame = self.layer.frame
            let layerBounds = self.layer.bounds
            let layerAnchorPoint = self.layer.anchorPoint
            if viewFrame == nil {
                let newPoint = childView.convert(point, from: self)
                if childView.bounds.contains(newPoint){
                    view = childView
                }
            }
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
    
    class ChildView:UIView{
        
        override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesBegan(touches, with: event)
            print("Child view touchesBegan")
        }
        
        override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesMoved(touches, with: event)
            print("Child view touchesMove")
        }
        
        override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesEnded(touches, with: event)
            print("Child view touchesEnded")
        }
        
        override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
            super.touchesCancelled(touches, with: event)
            print("Child view touchesBegan")
        }
        
        override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
            super.hitTest(point, with: event)
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
}
