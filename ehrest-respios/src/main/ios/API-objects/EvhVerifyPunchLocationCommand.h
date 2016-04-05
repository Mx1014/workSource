//
// EvhVerifyPunchLocationCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyPunchLocationCommand
//
@interface EvhVerifyPunchLocationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* companyId;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* longitude;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

