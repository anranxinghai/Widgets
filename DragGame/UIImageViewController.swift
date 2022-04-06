//
//  UIImageViewController.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/10.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import SnapKit
//import Alamofire
import Kingfisher
class UIImageViewController:UIViewController{
    private var uiImage:UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        uiImage = UIImageView.init()
        view.addSubview(uiImage)
        uiImage.snp.makeConstraints{(make)in
            make.width.height.equalTo(100)
            make.center.equalToSuperview()

        }
    
        let image = UIImage.init(imageLiteralResourceName: "anranxinghai.png")
        uiImage.image = image
    }
}
