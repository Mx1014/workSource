//
// EvhAddPunchExceptionRequestCommand.h
// generated at 2016-03-28 15:56:08 
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

