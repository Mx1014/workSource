//
// EvhListContactRespose.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactRespose
//
@interface EvhListContactRespose
    : NSObject<EvhJsonSerializable>


// item type EvhContactDTO*
@property(nonatomic, strong) NSMutableArray* contacts;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

