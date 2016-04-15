//
// EvhExchangeHallActionData.h
// generated at 2016-04-12 15:02:19 
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

