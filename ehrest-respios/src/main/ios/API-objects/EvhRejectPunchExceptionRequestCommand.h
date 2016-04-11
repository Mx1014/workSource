//
// EvhRejectPunchExceptionRequestCommand.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRejectPunchExceptionRequestCommand
//
@interface EvhRejectPunchExceptionRequestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* processDetail;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

