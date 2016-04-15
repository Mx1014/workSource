//
// EvhVerifyVideoConfAccountCommand.h
// generated at 2016-04-12 15:02:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyVideoConfAccountCommand
//
@interface EvhVerifyVideoConfAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* enterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

