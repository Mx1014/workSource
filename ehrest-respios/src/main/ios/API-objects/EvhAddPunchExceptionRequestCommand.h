//
// EvhAddPunchExceptionRequestCommand.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddPunchExceptionRequestCommand
//
@interface EvhAddPunchExceptionRequestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* punchDate;

@property(nonatomic, copy) NSString* requestDescription;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

