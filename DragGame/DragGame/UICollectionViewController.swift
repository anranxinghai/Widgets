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
//    private var collectionViewFlowLayout:CenterScaleLayout!
    
    private var collectionView:UICollectionView!
    private var dataSource:[String]!
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        var data = [String]()
        for i in 0...99{
            data.append("第\(i)个")
        }
       let  centerScaleLayout = CenterScaleLayout.init()
        dataSource = data
//        let itemSize = (SCREENWIDTH - 20)/3
//        collectionViewFlowLayout.itemSize = CGSize.init(width:itemSize , height: itemSize)
//        collectionViewFlowLayout.minimumLineSpacing = 10
//        collectionViewFlowLayout.minimumInteritemSpacing = 10
        collectionView = UICollectionView.init(frame:CGRect.init(x: 0, y: 0, width: SCREENWIDTH, height: SCREENHEIGHT),collectionViewLayout:centerScaleLayout)
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

fileprivate let ITEM_COUNT = 20
fileprivate let ITEM_SPACING:CGFloat = 16
fileprivate let ITEM_WIDTH:CGFloat = SCREENWIDTH - 160
fileprivate let ITEM_HEIGHT:CGFloat = 140

class CenterScaleLayout :UICollectionViewLayout{
    override var collectionViewContentSize: CGSize{
        return CGSize.init(width: ITEM_SPACING + CGFloat(ITEM_COUNT) * ITEM_WIDTH + ITEM_SPACING, height: ITEM_WIDTH)
    }
    
    override func shouldInvalidateLayout(forBoundsChange newBounds: CGRect) -> Bool {
        return true
    }
    
    private func getIndexPaths(inRect rect:CGRect)->[IndexPath]{
        var indexPaths = [IndexPath]()
        let start = max(Int(floor(Double(rect.origin.x / (ITEM_WIDTH + ITEM_SPACING)))), 0)
        let end = min(ITEM_COUNT - 1,Int(ceil(Double(rect.origin.x + rect.width)/(ITEM_WIDTH + ITEM_SPACING))))
        for i in start ... end{
            indexPaths.append(IndexPath.init(item: i, section: 0))
        }
        
        return indexPaths
        
    }
    
    override func layoutAttributesForElements(in rect: CGRect) -> [UICollectionViewLayoutAttributes]? {
        var elementAttributes = [UICollectionViewLayoutAttributes]()
        let indexPaths = getIndexPaths(inRect:rect)
        for indexPath in indexPaths {
            if let attributes = layoutAttributesForItem(at: indexPath){
                elementAttributes.append(attributes)
            }
        }
        return elementAttributes
    }
    
    override func layoutAttributesForItem(at indexPath: IndexPath) -> UICollectionViewLayoutAttributes? {
        let attributes = UICollectionViewLayoutAttributes.init(forCellWith: indexPath)
        let origin = ITEM_SPACING + CGFloat(indexPath.item) * (ITEM_SPACING + ITEM_WIDTH)
        attributes.frame = CGRect.init(x: origin, y: ITEM_HEIGHT / 2,width: ITEM_WIDTH, height: ITEM_WIDTH)
        if let collectionView = collectionView{
            let centerX = origin + ITEM_WIDTH/2
            let collectionViewCenterX = collectionView.contentOffset.x + collectionView.bounds.width / 2
            let offset = SCREENWIDTH/2 - fabs(collectionViewCenterX - centerX)
            var scale = 1.0
            if offset > 0{
                scale = 1 + offset/(SCREENWIDTH / 2.0) * 0.18
            }
            attributes.transform = CGAffineTransform.init(scaleX: scale, y: scale)
            attributes.zIndex = Int(scale * 1000)
            
        }
        return attributes
    }
}
