//
// EvhExchangeHallActionData.h
// generated at 2016-04-26 18:22:55 
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

