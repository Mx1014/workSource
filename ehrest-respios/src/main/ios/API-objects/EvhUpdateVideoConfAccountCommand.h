//
// EvhUpdateVideoConfAccountCommand.h
// generated at 2016-03-31 13:49:15 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateVideoConfAccountCommand
//
@interface EvhUpdateVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accountId;

@property(nonatomic, copy) NSNumber* validDate;

@property(nonatomic, copy) NSNumber* confType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

