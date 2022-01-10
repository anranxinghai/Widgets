//
//  UILabelViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/8.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
class UILabelViewController:UIViewController{
    private var uiLabel:UILabel?
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
                uiLabel = UILabel.init()
                uiLabel?.text = "UILabel"
                uiLabel?.textColor = .red
        view.addSubview(uiLabel!)
        uiLabel?.numberOfLines = 1
                
                uiLabel?.translatesAutoresizingMaskIntoConstraints = false
                uiLabel?.leadingAnchor.constraint(equalTo: view.leadingAnchor,constant: 100).isActive = true
        uiLabel?.topAnchor.constraint(equalTo: view.topAnchor,constant:  100).isActive = true
                
//                uiLabel?.widthAnchor.constraint(equalToConstant: 200).isActive = true
//                uiLabel?.heightAnchor.constraint(equalToConstant: 100).isActive = true
        
    }
}
