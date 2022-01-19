//
//  UICollectionView.swift
//  DragGame
//
//  Created by anranxinghai on 2022/1/17.
//  Copyright © 2022 anranxinghai. All rights reserved.
//

import Foundation
import UIKit
class UICVController :UIViewController{
    private var collectionViewFlowLayout:UICollectionViewFlowLayout!
    
    private var collectionView:UICollectionView!
    private var dataSource:[String]!
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        var data = [String]()
        for i in 0...99{
            data.append("第\(i)个")
        }
        collectionViewFlowLayout = UICollectionViewFlowLayout.init()
        dataSource = data
        let itemSize = (SCREENWIDTH - 20)/3
        collectionViewFlowLayout.itemSize = CGSize.init(width:itemSize , height: itemSize)
        collectionViewFlowLayout.minimumLineSpacing = 10
        collectionViewFlowLayout.minimumInteritemSpacing = 10
        collectionView = UICollectionView.init(frame:view.bounds,collectionViewLayout:collectionViewFlowLayout)
//        collectionView.collectionViewFlowLayout = collectionViewFlowLayout
        collectionView.dataSource = self
        collectionView.register(CustomCell.self, forCellWithReuseIdentifier: "CustomCell")
        view.addSubview(collectionView)
    }
}

extension UICVController: UICollectionViewDataSource, UICollectionViewDelegate{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int{
        return dataSource.endIndex
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell{
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CustomCell", for:indexPath)
        if indexPath.item < dataSource.endIndex, let cell = cell as? CustomCell{
            let number = dataSource[indexPath.item]
            cell.label.text = number
        }

        return cell
    }
}

class CustomCell:UICollectionViewCell{
    let label:UILabel!
    override init(frame: CGRect) {
        label = UILabel.init()
        label.textColor = .red
        label.textAlignment = .center
        super.init(frame: frame)
        contentView.addSubview(label)
        label.frame = CGRect.init(origin:.zero,size:frame.size)
        contentView.backgroundColor = .green
        contentView.clipsToBounds = true
    }
    
    required init?(coder: NSCoder) {
        fatalError("")
    }
}
