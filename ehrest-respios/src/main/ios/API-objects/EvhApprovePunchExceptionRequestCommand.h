//
// EvhApprovePunchExceptionRequestCommand.h
// generated at 2016-04-26 18:22:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApprovePunchExceptionRequestCommand
//
@interface EvhApprovePunchExceptionRequestCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* processCode;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

