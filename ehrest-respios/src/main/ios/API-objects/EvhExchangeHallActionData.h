//
// EvhExchangeHallActionData.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExchangeHallActionData
//
@interface EvhExchangeHallActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* tag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

