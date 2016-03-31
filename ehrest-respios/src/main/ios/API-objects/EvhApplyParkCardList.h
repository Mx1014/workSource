//
// EvhApplyParkCardList.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhApplyParkCardDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplyParkCardList
//
@interface EvhApplyParkCardList
    : NSObject<EvhJsonSerializable>


// item type EvhApplyParkCardDTO*
@property(nonatomic, strong) NSMutableArray* applyCard;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

