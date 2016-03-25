//
// EvhVerifyPunchLocationCommand.h
// generated at 2016-03-25 17:08:12 
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

