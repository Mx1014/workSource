//
// EvhPunchExceptionDTO.h
// generated at 2016-03-31 15:43:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchExceptionDTO
//
@interface EvhPunchExceptionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* exceptionComment;

@property(nonatomic, copy) NSNumber* requestType;

@property(nonatomic, copy) NSNumber* processCode;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

