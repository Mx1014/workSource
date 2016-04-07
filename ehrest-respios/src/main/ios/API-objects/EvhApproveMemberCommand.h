//
// EvhApproveMemberCommand.h
// generated at 2016-04-07 10:47:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApproveMemberCommand
//
@interface EvhApproveMemberCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* memberUid;

@property(nonatomic, copy) NSNumber* operatorRole;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

