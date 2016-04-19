//
// EvhAddSourceVideoConfAccountCommand.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddSourceVideoConfAccountCommand
//
@interface EvhAddSourceVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sourceAccount;

@property(nonatomic, copy) NSString* password;

@property(nonatomic, copy) NSNumber* accountCategory;

@property(nonatomic, copy) NSNumber* validDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

